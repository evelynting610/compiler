	.file	"ArrayLength.java"
	.section	.rodata

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
L1:
	movq %rsi, %rsi
	movq $8,%rdi
	subq %rdi, %rsi
	movq (%rsi), %rdi
	callq b_printString_0
main_0:
	jmp L0
L0:
	leave
	ret


