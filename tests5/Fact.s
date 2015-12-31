	.file	"Fact.java"
	.section	.rodata
L0:	.string	"\n"
	.align 8

	.align 8

	.section	.data

	.section	.text
	.align 8
	.globl main
	.type	main,@function
main:
	jmp	m_0
	.text
	.align 8
m_0:
	pushq	%rbp
	movq	%rsp,%rbp
	subq	$0, %rsp
L5:
	movq $10,%rdi
	callq m_1
	movq %rax, %rdi 
	callq b_intToString_0
	movq %rax, %rdi 
	leaq L0(%rip),%rsi
	callq b_stringConcatenate_0
	movq %rax, %rdi 
	callq b_printString_0
main_0:
	jmp L4
L4:
	leave
	ret


	.text
	.align 8
m_1:
	pushq	%rbp
	movq	%rsp,%rbp
	subq	$16, %rsp
L7:
	movq %r15,0(%rsp)
	movq %rdi, %rax 
	movq $1,%rdi
	cmpq %rdi, %rax 
	jle L1
L2:
	movq %rax, %r15 
	movq %rax, %rdi
	movq $1,%rsi
	subq %rsi, %rdi
	callq m_1
	movq %rax, %rdi 
	movq %r15, %rax
	imulq %rdi, %rax
fact_0:
	movq 0(%rsp),%r15
	jmp L6
L1:
	jmp fact_0
L8:
L3:
	jmp fact_0
L6:
	leave
	ret


