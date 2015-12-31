	.file	"T.java"
	.section	.rodata
L0:	.string	"Hello\n"
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
L2:
	leaq L0(%rip),%rdi
	callq b_printString_0
main_0:
	jmp L1
L1:
	leave
	ret


